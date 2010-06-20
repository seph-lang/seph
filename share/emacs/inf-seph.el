(require 'comint)
(require 'compile)
(require 'seph-mode)

;;;; for seph
(defvar seph-program-name "seph"
  "*Program invoked by the run-seph command")

(defvar inferior-seph-first-prompt-pattern "^seph>"
  "first prompt regex pattern of seph interpreter.")

(defvar inferior-seph-prompt-pattern "^seph>"
  "prompt regex pattern of seph interpreter.")

;;
;; mode variables
;;
(defvar inferior-seph-mode-hook nil
  "*Hook for customising inferior-seph mode.")
(defvar inferior-seph-mode-map nil
  "*Mode map for inferior-seph-mode")

(defconst inferior-seph-error-regexp-alist
       '(("SyntaxError: compile error\n^\\([^\(].*\\):\\([1-9][0-9]*\\):" 1 2)
	 ("^\tfrom \\([^\(].*\\):\\([1-9][0-9]*\\)\\(:in `.*'\\)?$" 1 2)))

(cond ((not inferior-seph-mode-map)
       (setq inferior-seph-mode-map
	     (copy-keymap comint-mode-map))
;       (define-key inferior-seph-mode-map "\M-\C-x" ;gnu convention
;	           'seph-send-definition)
;       (define-key inferior-seph-mode-map "\C-x\C-e" 'seph-send-last-sexp)
       (define-key inferior-seph-mode-map "\C-c\C-l" 'seph-load-file)
))

(defun inf-seph-keys ()
  "Set local key defs for inf-seph in seph-mode"
  (define-key seph-mode-map "\M-\C-x" 'seph-send-definition)
;  (define-key seph-mode-map "\C-x\C-e" 'seph-send-last-sexp)
  (define-key seph-mode-map "\C-c\C-b" 'seph-send-block)
  (define-key seph-mode-map "\C-c\M-b" 'seph-send-block-and-go)
  (define-key seph-mode-map "\C-c\C-x" 'seph-send-definition)
  (define-key seph-mode-map "\C-c\M-x" 'seph-send-definition-and-go)
  (define-key seph-mode-map "\C-c\C-r" 'seph-send-region)
  (define-key seph-mode-map "\C-c\M-r" 'seph-send-region-and-go)
  (define-key seph-mode-map "\C-c\C-z" 'switch-to-seph)
  (define-key seph-mode-map "\C-c\C-l" 'seph-load-file)
;  (define-key seph-mode-map "\C-c\C-s" 'run-seph)
)

(defvar seph-buffer nil "current seph process buffer.")

(defun inferior-seph-mode ()
  "Major mode for interacting with an inferior seph (seph) process.

The following commands are available:
\\{inferior-seph-mode-map}

A seph process can be fired up with M-x run-seph.

Customisation: Entry to this mode runs the hooks on comint-mode-hook and
inferior-seph-mode-hook (in that order).

You can send text to the inferior seph process from other buffers containing
Seph source.
    switch-to-seph switches the current buffer to the seph process buffer.
    seph-send-definition sends the current definition to the seph process.
    seph-send-region sends the current region to the seph process.

    seph-send-definition-and-go, seph-send-region-and-go,
        switch to the seph process buffer after sending their text.
For information on running multiple processes in multiple buffers, see
documentation for variable seph-buffer.

Commands:
Return after the end of the process' output sends the text from the 
    end of process to point.
Return before the end of the process' output copies the sexp ending at point
    to the end of the process' output, and sends it.
Delete converts tabs to spaces as it moves back.
Tab indents for io; with argument, shifts rest
    of expression rigidly with the current line.
C-M-q does Tab on each line starting within following expression.
Paragraphs are separated only by blank lines.  # start comments.
If you accidentally suspend your process, use \\[comint-continue-subjob]
to continue it."
  (interactive)
  (comint-mode)
  ;; Customise in inferior-seph-mode-hook
  ;(setq comint-prompt-regexp "^[^>\n]*>+ *")
  (setq comint-prompt-regexp inferior-seph-prompt-pattern)
  ;;(scheme-mode-variables)
;;  (seph-mode-variables)
  (setq major-mode 'inferior-seph-mode)
  (setq mode-name "Inferior Seph")
  (setq mode-line-process '(":%s"))
  (use-local-map inferior-seph-mode-map)
  (setq comint-input-filter (function seph-input-filter))
  (setq comint-get-old-input (function seph-get-old-input))
  (compilation-shell-minor-mode t)
  (make-local-variable 'compilation-error-regexp-alist)
  (setq compilation-error-regexp-alist inferior-seph-error-regexp-alist)
  (run-hooks 'inferior-seph-mode-hook))

(defvar inferior-seph-filter-regexp "\\`\\s *\\S ?\\S ?\\s *\\'"
  "*Input matching this regexp are not saved on the history list.
Defaults to a regexp ignoring all inputs of 0, 1, or 2 letters.")

(defun seph-input-filter (str)
  "Don't save anything matching inferior-seph-filter-regexp"
  (not (string-match inferior-seph-filter-regexp str)))

;; adapted from replace-in-string in XEmacs (subr.el)
(defun remove-in-string (str regexp)
  "Remove all matches in STR for REGEXP and returns the new string."
  (let ((rtn-str "") (start 0) match prev-start)
    (while (setq match (string-match regexp str start))
      (setq prev-start start
	    start (match-end 0)
	    rtn-str (concat rtn-str (substring str prev-start match))))
    (concat rtn-str (substring str start))))

(defun seph-get-old-input ()
  "Snarf the sexp ending at point"
  (save-excursion
    (let ((end (point)))
      (re-search-backward inferior-seph-first-prompt-pattern)
      (remove-in-string (buffer-substring (point) end)
			inferior-seph-prompt-pattern)
      )))

(defun seph-args-to-list (string)
  (let ((where (string-match "[ \t]" string)))
    (cond ((null where) (list string))
	  ((not (= where 0))
	   (cons (substring string 0 where)
		 (seph-args-to-list (substring string (+ 1 where)
						 (length string)))))
	  (t (let ((pos (string-match "[^ \t]" string)))
	       (if (null pos)
		   nil
		 (seph-args-to-list (substring string pos
						 (length string)))))))))

(defun run-seph (cmd)
  "Run an inferior Seph process, input and output via buffer *seph*.
If there is a process already running in `*seph*', switch to that buffer.
With argument, allows you to edit the command line (default is value
of `seph-program-name').  Runs the hooks `inferior-seph-mode-hook'
\(after the `comint-mode-hook' is run).
\(Type \\[describe-mode] in the process buffer for a list of commands.)"

  (interactive (list (if current-prefix-arg
			 (read-string "Run Seph: " seph-program-name)
			 seph-program-name)))
  (if (not (comint-check-proc "*seph*"))
      (let ((cmdlist (seph-args-to-list cmd)))
	(set-buffer (apply 'make-comint "seph" (car cmdlist)
			   nil (cdr cmdlist)))
	(inferior-seph-mode)))
  (setq seph-program-name cmd)
  (setq seph-buffer "*seph*")
  (pop-to-buffer "*seph*"))

(defconst seph-send-terminator "--inf-seph-%x-%d-%d-%d--"
  "Template for seph here document terminator.
Must not contain seph meta characters.")

(defconst seph-eval-separator "")

(defun seph-send-region (start end)
  "Send the current region to the inferior Seph process."
  (interactive "r")
  (let (term (file (buffer-file-name)) line)
    (save-excursion
      (save-restriction
	(widen)
	(goto-char start)
	(setq line (+ start (forward-line (- start)) 1))
	(goto-char start)
	(while (progn
		 (setq term (apply 'format seph-send-terminator (random) (current-time)))
		 (re-search-forward (concat "^" (regexp-quote term) "$") end t)))))
    ;; compilation-parse-errors parses from second line.
    (save-excursion
      (let ((m (process-mark (seph-proc))))
	(set-buffer (marker-buffer m))
	(goto-char m)
	(insert seph-eval-separator "\n")
	(set-marker m (point))))
    (comint-send-string (seph-proc) (format "eval <<'%s', nil, %S, %d\n" term file line))
    (comint-send-region (seph-proc) start end)
    (comint-send-string (seph-proc) (concat "\n" term "\n"))))

(defun seph-send-definition ()
  "Send the current definition to the inferior Seph process."
  (interactive)
  (save-excursion
    (seph-end-of-defun)
    (let ((end (point)))
      (seph-beginning-of-defun)
      (seph-send-region (point) end))))

;(defun seph-send-last-sexp ()
;  "Send the previous sexp to the inferior Seph process."
;  (interactive)
;  (seph-send-region (save-excursion (backward-sexp) (point)) (point)))

(defun seph-send-block ()
  "Send the current block to the inferior Seph process."
  (interactive)
  (save-excursion
    (seph-end-of-block)
    (end-of-line)
    (let ((end (point)))
      (seph-beginning-of-block)
      (seph-send-region (point) end))))

(defun switch-to-seph (eob-p)
  "Switch to the seph process buffer.
With argument, positions cursor at end of buffer."
  (interactive "P")
  (if (get-buffer seph-buffer)
      (pop-to-buffer seph-buffer)
      (error "No current process buffer. See variable seph-buffer."))
  (cond (eob-p
	 (push-mark)
	 (goto-char (point-max)))))

(defun seph-send-region-and-go (start end)
  "Send the current region to the inferior Seph process.
Then switch to the process buffer."
  (interactive "r")
  (seph-send-region start end)
  (switch-to-seph t))

(defun seph-send-definition-and-go ()
  "Send the current definition to the inferior Seph. 
Then switch to the process buffer."
  (interactive)
  (seph-send-definition)
  (switch-to-seph t))

(defun seph-send-block-and-go ()
  "Send the current block to the inferior Seph. 
Then switch to the process buffer."
  (interactive)
  (seph-send-block)
  (switch-to-seph t))

(defvar seph-source-modes '(seph-mode)
  "*Used to determine if a buffer contains Seph source code.
If it's loaded into a buffer that is in one of these major modes, it's
considered a seph source file by seph-load-file.
Used by these commands to determine defaults.")

(defvar seph-prev-l/c-dir/file nil
  "Caches the last (directory . file) pair.
Caches the last pair used in the last seph-load-file command.
Used for determining the default in the 
next one.")

(defun seph-load-file (file-name)
  "Load a Seph file into the inferior Seph process."
  (interactive (comint-get-source "Load Seph file: " seph-prev-l/c-dir/file
				  seph-source-modes t)) ; T because LOAD 
                                                          ; needs an exact name
  (comint-check-source file-name) ; Check to see if buffer needs saved.
  (setq seph-prev-l/c-dir/file (cons (file-name-directory    file-name)
				       (file-name-nondirectory file-name)))
  (comint-send-string (seph-proc) (concat "(load \""
					    file-name
					    "\"\)\n")))

(defun seph-proc ()
  "Returns the current seph process. See variable seph-buffer."
  (let ((proc (get-buffer-process (if (eq major-mode 'inferior-seph-mode)
				      (current-buffer)
				    seph-buffer))))
    (or proc
	(error "No current process. See variable seph-buffer"))))

;;; Do the user's customisation...

(defvar inf-seph-load-hook nil
  "This hook is run when inf-seph is loaded in.
This is a good place to put keybindings.")
	
(run-hooks 'inf-seph-load-hook)

(provide 'inf-seph)
